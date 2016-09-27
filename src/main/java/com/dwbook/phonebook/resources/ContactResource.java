package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.ContactDAO;
import com.dwbook.phonebook.representations.Contact;
import org.skife.jdbi.v2.DBI;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by objectedge on 9/26/16.
 */

@Path("/contact")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

    private final ContactDAO contactDAO;
    private final Validator validator;

    public ContactResource(DBI jdbi, Validator validator){
        contactDAO = jdbi.onDemand(ContactDAO.class);
        this.validator = validator;
    }

    @GET
    @Path("/{id}")
    public Response getContact(@PathParam("id") int id){
        // retrieve information about the contact with the provided id
        Contact contact = contactDAO.getContactById(id);
        return Response.ok(contact).build();
    }

    @POST
    public Response createContact(Contact contact) throws URISyntaxException{

        //validate the contact's data
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);
        // Are there any constraint violations?
        if(!violations.isEmpty()){
            //validations errors occurred
            List<String> validationMessage = new ArrayList<>();
            for (ConstraintViolation<Contact> violation : violations){
                validationMessage.add(violation.getPropertyPath().toString()+ " : "+ violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessage).build();
        }else{
            //no errors
            //update de contact with the provided id
            int newContactId = contactDAO.createContact(contact.getFirstName(),
                    contact.getLastName(), contact.getPhone());
            return Response.created(new URI(String.valueOf(newContactId))).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteContact(@PathParam("id") int id){
        // delete de contact with the provided id
        contactDAO.deleteContact(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateContact(@PathParam("id") int id, Contact contact){

        //validate the updated data
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);
        //Are there any constraint violations?
        if(!violations.isEmpty()){
            //validations errors occurred
            List<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<Contact> violation : violations){
                validationMessages.add(violation.getPropertyPath().toString() + " : " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }else{
            //no errors
            // update the contact with the provided ID
            contactDAO.updateContact(id, contact.getFirstName(), contact.getLastName(), contact.getPhone());
            return Response.ok(new Contact(id, contact.getFirstName(), contact.getLastName(), contact.getPhone())).build();
        }
    }

}
