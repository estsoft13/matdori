package com.estsoft13.matdori.repository;

import com.estsoft13.matdori.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
