package org.example.tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.tickets.dto.TicketRequestDto;
import org.example.tickets.dto.TicketResponseDto;
import org.example.tickets.exceptions.ExceptionResponse;
import org.example.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.Link;

@Tag(name = "Ticket", description = "Here are all the endpoints of this api")
@RequestMapping("/api/tickets/v1")
@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PagedResourcesAssembler<TicketResponseDto> assembler;

    @Operation(summary = "Operation to create a ticket", description = "This operation creates a new ticket",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object that represents the ticket",
                required = true,
                content = @Content(schema = @Schema(implementation = TicketRequestDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Resource successfully created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Event id not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "422", description = "Trying to create a ticket with invalid data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
        })
    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponseDto> createTicket(@RequestBody @Valid TicketRequestDto ticketRequestDto) {
        return ResponseEntity.ok().body(ticketService.create(ticketRequestDto));
    }

    @Operation(summary = "Operation to retrieve a ticket by its own id", description = "This operation return the ticket bi its own id",
            parameters = @Parameter(
                    name = "id",
                    description = "Ticket unique ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket id not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable String id) {
        return ResponseEntity.ok().body(ticketService.findById(id));
    }

    @Operation(summary = "Operation returns tickets by CPF", description = "This operation returns a list of tickets by CPF",
            parameters = {
                @Parameter(
                        name = "cpf",
                        description = "Client CPF",
                        required = true,
                        example = "55744793038"
                ),
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
                    @ApiResponse(responseCode = "200", description = "Resource successfully found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            })
    @GetMapping("/get-ticket-by-cpf/{cpf}")
    public ResponseEntity<PagedModel<EntityModel<TicketResponseDto>>> getTicketByCpf(
                @PathVariable String cpf,
                @RequestParam(value = "page", defaultValue = "0") Integer page,
                @RequestParam(value = "size", defaultValue = "10") Integer size
            ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketResponseDto> dtos =  ticketService.findByCpf(cpf, pageable);
        Link link = linkTo(methodOn(TicketController.class)
                .getTicketByCpf(cpf, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return ResponseEntity.ok().body(assembler.toModel(dtos, link));
    }

    @Operation(summary = "Operation to update a ticket", description = "This operation update an existent ticket",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Object that represents the ticket that will replace the old",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TicketRequestDto.class))
            ),
            parameters = @Parameter(
                    name = "id",
                    description = "Ticket unique ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Event id not found or ticket id not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Trying to create a ticket with invalid data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@PathVariable String id, @RequestBody @Valid TicketRequestDto ticketRequestDto) {
        return ResponseEntity.ok().body(ticketService.update(ticketRequestDto, id));
    }

    @Operation(summary = "Operation to delete a ticket", description = "This operation deletes a ticket by its own id",
            parameters = @Parameter(
                    name = "id",
                    description = "Ticket unique ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resource successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Ticket id not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            })
    @DeleteMapping("/cancel-ticket/{id}")
    public ResponseEntity<TicketResponseDto> cancelTicket(@PathVariable String id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Operation returns tickets by event id", description = "This operation returns a list of tickets by event id",
            parameters = {
                    @Parameter(
                            name = "eventId",
                            description = "Event id",
                            required = true,
                            example = "123e4567-e89b-12d3-a456-426614174000"
                    ),
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
                    @ApiResponse(responseCode = "200", description = "Resource successfully found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            })
    @GetMapping("/check-tickets-by-event/{eventId}")
    public ResponseEntity<PagedModel<EntityModel<TicketResponseDto>>> checkTicketsByEvent(
            @PathVariable String eventId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketResponseDto> dtos =  ticketService.findAllByEventId(eventId, pageable);
        Link link = linkTo(methodOn(TicketController.class)
                .checkTicketsByEvent(eventId, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return ResponseEntity.ok().body(assembler.toModel(dtos, link));
    }
}
