package org.acme.book;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.pagination.PageResult;

import java.net.URI;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService bookService;

    @POST
    @Path("/search")
    public PageResult<Book> search(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sort") String sort,
            BookFilter bookFilter
    ) {
        return bookService.searchBooks(page, size, sort, bookFilter);
    }

    @POST
    @Transactional
    public Response createBook(
           Book incoming,
           @Context UriInfo uriInfo
    ) {

        if (incoming == null || incoming.getTitle() == null || incoming.getTitle().isBlank()) {
            throw new BadRequestException("Field 'name' is required.");
        } else {
            if (incoming.getCategory() == null) {
                throw new BadRequestException("Field 'category' is required.");
            }
        }

        Book book = bookService.createBook(incoming);
        URI location = uriInfo.getAbsolutePathBuilder().path(book.getId().toString()).build();
        return Response.created(location).entity(book).build();

    }

    @DELETE()
    @Path("{id}")
    @Transactional
    public void deleteBook(@PathParam("id") Long id) {
        bookService.deleteBook(id);
    }
}
