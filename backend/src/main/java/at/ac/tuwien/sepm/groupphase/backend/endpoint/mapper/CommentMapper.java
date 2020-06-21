package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommentRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
