package ch.zhaw.babinames.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// TODO: Annotationen für Getter, Konstruktor und toString einfügen
@Getter
@AllArgsConstructor
@ToString
public class Name {
    private String name;
    private String geschlecht;
    private int anzahl;
}