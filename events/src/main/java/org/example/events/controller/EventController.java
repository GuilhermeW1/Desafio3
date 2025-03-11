package org.example.events.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.events.dto.EventRequestDto;
import org.example.events.dto.EventResponseDto;
import org.example.events.exceptions.ExceptionResponse;
import org.example.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Events", description = "Here are all the endpoints of this api")
@RestController
@RequestMapping("/api/events/v1")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PagedResourcesAssembler<EventResponseDto> assembler;


    @Operation(summary = "Operation to create a event", description = "This operation creates a new event",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Object that represents the event",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cep not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Trying to create a event with invalid data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDto> create(@RequestBody @Valid EventRequestDto event) {
        return ResponseEntity.ok().body(eventService.create(event));
    }

    @Operation(summary = "Operation to retrieve a event by its own id", description = "This operation return the event by its own id",
            parameters = @Parameter(
                    name = "id",
                    description = "event unique ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "event id not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable String id) {
        return ResponseEntity.ok().body(eventService.findById(id));
    }

    @Operation(summary = "Operation returns all events", description = "This operation returns a list of events",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Represents the returned page"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", description = "10")),
                            description = "Represents the total of elements per page"
                    ),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resources successfully found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            })
    @GetMapping("/get-all-events")
    public ResponseEntity<PagedModel<EntityModel<EventResponseDto>>> getAllEvents(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventResponseDto> dtos =  eventService.findAll(pageable);
        dtos.map(e -> e.add(linkTo(methodOn(EventController.class).getEvent(e.getId())).withSelfRel()));
        Link link = linkTo(methodOn(EventController.class)
                .getAllEvents(pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return ResponseEntity.ok().body(assembler.toModel(dtos, link));
    }

    @Operation(summary = "Operation returns all events", description = "This operation returns a list of events",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Represents the returned page"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", description = "10")),
                            description = "Represents the total of elements per page"
                    ),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resources successfully found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            })
    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<PagedModel<EntityModel<EventResponseDto>>> getAllEventsSorted(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(dir, "eventName");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EventResponseDto> dtos =  eventService.findAllSorted(pageable);
        dtos.map(e -> e.add(linkTo(methodOn(EventController.class).getEvent(e.getId())).withSelfRel()));
        Link link = linkTo(methodOn(EventController.class)
                .getAllEvents(pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return ResponseEntity.ok().body(assembler.toModel(dtos, link));
    }



    @Operation(summary = "Operation to update a event", description = "This operation update a new event",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Object that represents the event",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventRequestDto.class))
            ),
            parameters = @Parameter(
                    name = "id",
                    description = "event unique ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully update",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cep not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Trying to create a event with invalid data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @PutMapping("update-event/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable String id, @RequestBody @Valid EventRequestDto event) {
        return ResponseEntity.ok().body(eventService.update(event, id));
    }

    @Operation(summary = "Operation to delete a event", description = "This operation deletes a event by its own id",
            parameters = @Parameter(
                    name = "id",
                    description = "Ticket unique ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resource successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Event id not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "409", description = "There are tickets associate with this event",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
