package API;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import EJB.Calcu;

import java.util.List;

@Stateless
@Path("/c")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalcuServices {

    @PersistenceContext
    private EntityManager entityManager;

    @POST
    @Path("/calc")
    public Response createCalculation(Calcu calculation) {
        try {
            int result = performCalculation(calculation.getNumber1(), calculation.getNumber2(), calculation.getOperation());
            entityManager.persist(calculation);
            return Response.status(Response.Status.OK).entity("{\"Result\": " + result + "}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"Error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/calculations")
    public Response getCalculations() {
        try {
            List<Calcu> calculations = entityManager.createQuery("SELECT c FROM Calcu c", Calcu.class).getResultList();
            return Response.status(Response.Status.OK).entity(calculations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"Error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    private int performCalculation(int number1, int number2, String operation) {
        switch (operation) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "*":
                return number1 * number2;
            case "/":
                return number1 / number2;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }
    }
}
