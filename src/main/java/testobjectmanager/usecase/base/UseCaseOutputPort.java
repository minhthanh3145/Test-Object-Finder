package testobjectmanager.usecase.base;

public interface UseCaseOutputPort<T, V> {
    V getViewModel();

    void present(T response);
}
