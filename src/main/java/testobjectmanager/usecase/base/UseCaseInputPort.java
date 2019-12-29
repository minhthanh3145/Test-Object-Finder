package testobjectmanager.usecase.base;

import testobjectmanager.exceptions.ConstraintViolatedException;
import testobjectmanager.exceptions.RepositoryException;

public interface UseCaseInputPort<T, E> {

    /**
     * This method reflects what a use case does: directing the flow of data in and out of
     * business entities. Subclasses will have to override the templates (excepted this function itself)
     * to provide customized behaviors
     * 
     * @param request
     */
    default void execute(T request) {
        E response = null;
        try {
            validateContrainstsOfRequest(request);
            callRepositoryToOperateOnData(request);
            response = constructResponseOnSuccess();
        } catch (Exception e) {
            response = constructResponseOnFailure(e);
        } finally {
            presentResponse(response);
        }
    }

    void presentResponse(E response);

    E constructResponseOnFailure(Exception e);

    E constructResponseOnSuccess();

    void callRepositoryToOperateOnData(T request) throws RepositoryException;

    void validateContrainstsOfRequest(T request) throws ConstraintViolatedException;

}
