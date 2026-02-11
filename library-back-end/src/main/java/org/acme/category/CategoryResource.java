package org.acme.category;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @POST
    @Transactional
    public Response createCategory(Category incoming, @Context UriInfo uriInfo) {

        if (incoming == null || incoming.getName() == null || incoming.getName().isEmpty()) {
            throw new BadRequestException("Category name is null or empty");
        }

        Category category = categoryService.createCategory(incoming);
        URI location = uriInfo.getAbsolutePathBuilder().path(category.id.toString()).build();
        return Response.created(location).entity(category).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteCategory(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);
    }
}
