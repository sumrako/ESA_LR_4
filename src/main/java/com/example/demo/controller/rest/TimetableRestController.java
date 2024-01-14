package com.example.demo.controller.rest;

import com.example.demo.dto.TimetableDto;
import com.example.demo.service.TimetableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/rest/timetable")
@RequiredArgsConstructor
public class TimetableRestController {
    private final TimetableService timetableService;
    private final XmlMapper xmlMapper = new XmlMapper();
    @GetMapping(path = "", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> findAllXml() throws TransformerException, IOException {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        xmlMapper.registerModule(javaTimeModule);        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(new StreamSource("src/main/resources/static/timetable.xslt"));
        Source xmlSource = new StreamSource(new ByteArrayInputStream(xmlMapper.writeValueAsBytes(timetableService.findAll())));
        StringWriter out = new StringWriter();
        Result result = new StreamResult(out);
        transformer.transform(xmlSource, result);
        return ResponseEntity.status(HttpStatus.OK).body(out.toString());
    }

    @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findAllJson() {
        return ResponseEntity.status(HttpStatus.OK).body(timetableService.findAll());
    }

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        TimetableDto timetableDto = timetableService.findById(id);
        if (timetableDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(timetableDto);
    }

    @PostMapping(path = "", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> save(@RequestBody TimetableDto timetableDto) {
        timetableService.save(timetableDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        timetableService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TimetableDto timetableDto) {
        timetableDto.setId(id);
        timetableService.save(timetableDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
