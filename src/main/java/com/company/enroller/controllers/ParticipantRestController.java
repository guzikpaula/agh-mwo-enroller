package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String key) {

        Collection<Participant> participants;

        if (key != null && !key.isEmpty()) {
            participants = participantService.getAllFilteredByLogin(key);
        } else if ("login".equalsIgnoreCase(sortBy)) {
            participants = participantService.getAllSortedByLogin(sortOrder);
        } else {
            participants = participantService.getAll();
        }

        return new ResponseEntity<>(participants, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {

        Participant existing = participantService.findByLogin(participant.getLogin());
        if (existing != null) {
            return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
        }

        participantService.add(participant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.delete(participant);

        return new ResponseEntity<Participant>(participant, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@RequestBody Participant updatedParticipant, @PathVariable("id") String login) {

        Participant existing = participantService.findByLogin(login);
        if (existing == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        existing.setPassword(updatedParticipant.getPassword());

        participantService.update(existing);


        return new ResponseEntity<>(HttpStatus.OK);
    }


}
