package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @Column
    private Integer id;

    @NonNull
    @Column(nullable = false, length=1000000)
    private String text;

    @NonNull
    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.MERGE})
  //  @MapsId("id")
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

  /**  @NonNull
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("event_id")
    @JoinColumn(name = "event_id")
    private Event event; **/

    @NonNull
    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.MERGE})
   // @MapsId("id")
    @JoinColumn(name = "event_id")
    private Event event;
}

