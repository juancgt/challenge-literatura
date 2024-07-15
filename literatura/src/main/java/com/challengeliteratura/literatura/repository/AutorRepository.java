package com.challengeliteratura.literatura.repository;

import com.challengeliteratura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
