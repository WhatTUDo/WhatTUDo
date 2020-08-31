package at.whattudo.endpoint.mapper;


import at.whattudo.endpoint.dto.CommentDto;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Comment;
import at.whattudo.entity.Event;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.CommentRepository;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.UserRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected UserRepository userRepository;


    public abstract CommentDto commentToCommentDto(Comment comment);

    @BeforeMapping
    protected void mapEvents(Comment comment, @MappingTarget CommentDto commentDto) {
        Comment comEntity = commentRepository.findById(comment.getId())
            .orElseThrow(() -> new NotFoundException("This comment does not exist in the database"));

        commentDto.setEventId(comEntity.getEvent().getId());
        commentDto.setUsername(comEntity.getUser().getName());
        commentDto.setUpdateDateTime(comEntity.getUpdateDateTime());
    }

    public abstract Comment commentDtoToComment(CommentDto commentDto);

    @BeforeMapping
    public void mapEvents(CommentDto commentDto, @MappingTarget Comment comment) {



        Event eventEntity = eventRepository.findById(commentDto.getEventId())
            .orElseThrow(() -> new NotFoundException("The event for this comment does not exist in the database"));

        comment.setEvent(eventEntity);


        ApplicationUser userEntity = userRepository.findByName(commentDto.getUsername())
            .orElseThrow(() -> new NotFoundException("The user for this comment does not exist in the database"));

        comment.setUser(userEntity);
    }

}
