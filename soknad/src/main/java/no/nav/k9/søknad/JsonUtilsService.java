package no.nav.k9.søknad;

public interface JsonUtilsService {
    String toString(Object object);


    <T> T fromString(String s, Class<T> clazz);
}
