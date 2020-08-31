package at.whattudo.entity;

import lombok.*;

import javax.persistence.*;

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

