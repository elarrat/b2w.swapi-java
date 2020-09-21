package br.com.b2w.models.swapi;

/**
 * Representa a resposta padrão para uma coleção da SWAPI
 */
public class SwapiCollection<T> {
    private String count;
    private String next;
    private String previous;
    private T[] results;

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNext() {
        return this.next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return this.previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public T[] getResults() {
        return this.results;
    }

    public void setResults(T[] results) {
        this.results = results;
    }

}
