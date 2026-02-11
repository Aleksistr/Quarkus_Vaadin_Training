package org.acme.book;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.pagination.PageRequest;
import org.acme.pagination.PageResult;
import org.acme.pagination.SortRequest;

import java.net.URI;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService bookService;

    @GET
    public PageResult<Book> getPaged(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("id") String sortBy,
            @QueryParam("direction") @DefaultValue("ASC") String direction,
            @QueryParam("name") String name
    ) {

        SortRequest.Direction dir =
                "DESC".equalsIgnoreCase(direction)
                        ? SortRequest.Direction.DESC
                        : SortRequest.Direction.ASC;

        PageRequest pageable = new PageRequest(
                page,
                size,
                new SortRequest(sortBy, dir)
        );

        PanacheQuery<Book> query;

        if (name != null && !name.isBlank()) {
            query = Book.find("lower(name) like ?1", pageable.toPanacheSort(),
                    "%" + name.toLowerCase() + "%");
        } else {
            query = Book.findAll(pageable.toPanacheSort());
        }

        long total = query.count();
        query.page(pageable.toPanachePage());

        return new PageResult<>(
                query.list(),
                total,
                pageable.page,
                pageable.size
        );

    }

    @POST
    @Transactional
    public Response createBook(
           Book incoming,
           @Context UriInfo uriInfo
    ) {

        if (incoming == null || incoming.getTitle() == null || incoming.getTitle().isBlank()) {
            throw new BadRequestException("Field 'name' is required.");
        }

        Book book = bookService.createBook(incoming);
        URI location = uriInfo.getAbsolutePathBuilder().path(book.id.toString()).build();
        return Response.created(location).entity(book).build();

    }

    @DELETE()
    @Path("{id}")
    @Transactional
    public void deleteBook(@PathParam("id") Long id) {
        bookService.deleteBook(id);
    }
}
