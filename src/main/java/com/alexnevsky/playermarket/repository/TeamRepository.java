package com.alexnevsky.playermarket.repository;

import com.alexnevsky.playermarket.model.Team;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

  List<Team> findAllByIdIn(Collection<Long> ids);
}
