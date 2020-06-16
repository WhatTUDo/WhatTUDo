package at.ac.tuwien.sepm.groupphase.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;


    @NonNull
    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

}
