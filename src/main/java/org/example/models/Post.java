package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private String text;
    private LocalDateTime time;

    public Post(User user, String text) {
        this.user = user;
        this.text = text;
        this.time = LocalDateTime.now();
    }

    @Override
    public int compareTo(Post o) {
        return getTime().compareTo(o.getTime());
    }
}
