package ru.sbt.rgrtu.gol.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sbt.rgrtu.gol.entity.Board;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {

    Board findTop1ByGenerationAndXAndY(int generation, int x, int y);

}
