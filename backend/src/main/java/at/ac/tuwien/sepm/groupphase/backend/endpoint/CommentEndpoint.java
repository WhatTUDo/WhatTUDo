package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommentMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CommentService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(value = CommentEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentEndpoint {
    static final String BASE_URL = "/comments";
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public CommentDto getById(@PathVariable("id") Integer id) {
        try {
            return commentMapper.commentToCommentDto(commentService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @RolesAllowed({"MOD","MEMBER", "SYSADMIN"})
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete comment", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable("id") Integer id) {
        try {
            commentService.delete(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("authentication.name != null")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create comment", authorizations = {@Authorization(value = "apiKey")})
    public CommentDto create(@RequestBody CommentDto commentDto) {
        try {

            Comment comment = commentMapper.commentDtoToComment(commentDto);


            return commentMapper.commentToCommentDto(commentService.save(comment));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("#dto.username == authentication.name")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @ApiOperation(value = "Update comment", authorizations = {@Authorization(value = "apiKey")})
    public CommentDto update(@RequestBody CommentDto commentDto) {
        try {
            Comment comment = commentMapper.commentDtoToComment(commentDto);
            return commentMapper.commentToCommentDto(commentService.update(comment));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all Comments", authorizations = {@Authorization(value = "apiKey")})
    public List<CommentDto> getAllComments() {
        try {
            return commentService.getAll().stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @Transactional
    @GetMapping(value = "/users/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get Comments by User Id", authorizations = {@Authorization(value = "apiKey")})
    public List<CommentDto> getCommentsByUserId(@PathVariable(value = "id") int id) {
        try {

            List<CommentDto> results = new ArrayList<>();

            (commentService.findByUserId(id)).forEach(it -> results.add(commentMapper.commentToCommentDto(it)));


            return results;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
