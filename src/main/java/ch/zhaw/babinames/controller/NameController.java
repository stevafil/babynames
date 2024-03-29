package ch.zhaw.babinames.controller;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;
import ch.zhaw.babinames.model.Name;

@RestController
public class NameController {
    private ArrayList<Name> listOfNames;

    @GetMapping("/names")
    public List<Name> getNames() {
        return listOfNames;
    }

    @GetMapping("/names/count")
    public long getCount(@RequestParam(required = false) String sex) {
        if (sex != null) {
            return listOfNames.stream().filter(x -> x.getGeschlecht().equals(sex)).count();
        } else {
            return listOfNames.size();
        }
    }

    @GetMapping("/names/frequency")
    public int getFrequency(@RequestParam String name) {
        return listOfNames.stream()
            .filter(x -> x.getName().equalsIgnoreCase(name))
            .map(x -> x.getAnzahl())
            .findFirst().orElse(0);
    }

    
@GetMapping("/names/name")
public ResponseEntity<List<Name>> filterNames(
        @RequestParam String sex, 
        @RequestParam String start, 
        @RequestParam int length) {
    
    if (start == null || length <= 0 || !(sex.equals("m") || sex.equals("w"))) {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    String capitalizedStart = start.substring(0, 1).toUpperCase() + start.substring(1).toLowerCase();
    String upperCaseSex = sex.toUpperCase();

    List<Name> names = listOfNames.stream()
    .filter(x -> x.getName().startsWith(capitalizedStart)) // Vergleiche mit kapitalisiertem Start
    .filter(x -> x.getName().length() == length)
    .filter(x -> x.getGeschlecht().toUpperCase().equals(upperCaseSex)) // Vergleiche Großbuchstaben
    .collect(Collectors.toList());

    if (names.isEmpty()) {
        return new ResponseEntity<>(names, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(names, HttpStatus.OK);
}

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws Exception {
        listOfNames = new ArrayList<>();
        Path path = Paths.get(ClassLoader.getSystemResource("data/babynames.csv").toURI());
        System.out.println("Read from: " + path);
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    listOfNames.add(new Name(line[0], line[1], Integer.parseInt(line[2])));
                }
                System.out.println("Read " + listOfNames.size() + " names");
            }
        }
    }
}
