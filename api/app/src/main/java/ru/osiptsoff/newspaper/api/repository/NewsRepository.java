package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
