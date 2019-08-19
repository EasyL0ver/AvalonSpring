package common;

public interface EventHandler<T> {
    void Handle(T params);
}
