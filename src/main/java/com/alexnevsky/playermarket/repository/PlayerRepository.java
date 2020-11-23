package com.alexnevsky.playermarket.repository;

import com.alexnevsky.playermarket.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
