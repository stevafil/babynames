package ch.zhaw.babinames.model;

public class Name {
    private String name;
    private String geschlecht;
    private int anzahl;

    public Name(String name, String geschlecht, int anzahl) {
        this.name = name;
        this.geschlecht = geschlecht;
        this.anzahl = anzahl;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name [name=" + name + ", geschlecht=" + geschlecht + ", anzahl=" + anzahl + "]";
    }
}
