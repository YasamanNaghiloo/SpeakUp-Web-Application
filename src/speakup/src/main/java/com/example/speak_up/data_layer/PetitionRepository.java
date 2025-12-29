package com.example.speak_up.data_layer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO;
import com.example.speak_up.business_logic.dto.DetailViewPetitionDTO;
import com.example.speak_up.business_logic.entity.Petition;
import com.example.speak_up.business_logic.enums.Category;
import com.example.speak_up.business_logic.enums.LocationScope;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
    @Query(value = "SELECT COUNT(*) FROM signees WHERE petition_id = :petitionId", nativeQuery = true)
    long countByPetitionIdNative(@Param("petitionId") Long petitionId);

    @Query(value = "SELECT p.p_id, (SELECT COUNT(*) FROM signees s WHERE s.petition_id = p.p_id) AS signer_count FROM petitions p", nativeQuery = true)
    List<Object[]> countSignersForAllPetitions();

    @EntityGraph(attributePaths = { "owner", "owner.firstName", "owner.lastName", "owner.username" })
    @Query("SELECT p FROM Petition p")
    Page<Petition> findAllWithOwner(Pageable pageable);

    @EntityGraph(attributePaths = { "owner" })
    @Query("SELECT p FROM Petition p WHERE p.id = :id")
    Optional<Petition> findByIdWithOwner(@Param("id") Long id);

    @EntityGraph(attributePaths = { "owner", "responsible" })
    @Query("SELECT p FROM Petition p WHERE p.id = :id")
    Optional<Petition> findByIdWithOwnerAndResponsibles(@Param("id") Long id);

    @Query("SELECT p, COUNT(s) FROM Petition p LEFT JOIN Signee s ON s.petition = p " +
           "WHERE p.owner.id = :ownerId GROUP BY p")
    List<Object[]> findPetitionsAndSigneeCountByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT COUNT(p) FROM Petition p WHERE p.owner.id = :userId AND p.endDate >= :today")
    long countActivePetitionsByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
            "FROM Petition p " +
            "LEFT JOIN Signee s ON s.petition = p " +
            "GROUP BY p")
    Page<BrowseViewPetitonDTO> findPetitionsSorted(Pageable pageable);

    @Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
            "FROM Petition p " +
            "LEFT JOIN Signee s ON s.petition = p " +
            "GROUP BY p " +
            "ORDER BY COUNT(s) DESC")
    Page<BrowseViewPetitonDTO> findAllOrderBySignaturesDesc(Pageable pageable);

    @Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
            "FROM Petition p " +
            "LEFT JOIN Signee s ON s.petition = p " +
            "GROUP BY p " +
            "ORDER BY COUNT(s) ASC")
    Page<BrowseViewPetitonDTO> findAllOrderBySignaturesAsc(Pageable pageable);

    @Query("""
    SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s))
    FROM Petition p
    LEFT JOIN Signee s ON s.petition = p
    WHERE (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (
          :scope IS NULL OR
          (p.locationScope = :scope AND
            (
              (:scope != 'CITY' OR LOWER(p.locationCity) LIKE LOWER(CONCAT('%', :location, '%'))) AND
              (:scope != 'COUNTRY' OR LOWER(p.locationCountry) LIKE LOWER(CONCAT('%', :location, '%')))
            )
          )
      )
      AND (:category IS NULL OR p.category = :category)
      AND (:userName IS NULL OR p.owner.username = :userName)
    GROUP BY p
    """)
    Page<BrowseViewPetitonDTO> findPetitionsByScopeAndLocation(@Param("title") String title,
            @Param("scope") LocationScope scope,
            @Param("category") Category category,
            @Param("location") String location,
            @Param("userName") String userName,
            Pageable pageable);

 @Query("""
    SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s))
    FROM Petition p
    LEFT JOIN Signee s ON s.petition = p
    WHERE (:search IS NULL OR 
           LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR 
           LOWER(p.owner.username) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (
          :scope IS NULL OR
          (p.locationScope = :scope AND
            (
              (:scope != 'CITY' OR LOWER(p.locationCity) LIKE LOWER(CONCAT('%', :location, '%'))) AND
              (:scope != 'COUNTRY' OR LOWER(p.locationCountry) LIKE LOWER(CONCAT('%', :location, '%')))
            )
          )
      )
      AND (:category IS NULL OR p.category = :category)
    GROUP BY p
    """)
    Page<BrowseViewPetitonDTO> findPetitionsByScopeAndLocationbar(@Param("search") String search,
            @Param("scope") LocationScope scope,
            @Param("category") Category category,
            @Param("location") String location,
            Pageable pageable);

   @Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
       "FROM Petition p " +
       "LEFT JOIN Signee s ON s.petition = p " +
       "WHERE (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
       "AND (" +
       "  :scope IS NULL OR " +
       "  (p.locationScope = :scope AND " +
       "    ( " +
       "      (:scope != 'CITY' OR LOWER(p.locationCity) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
       "      (:scope != 'COUNTRY' OR LOWER(p.locationCountry) LIKE LOWER(CONCAT('%', :location, '%'))) " +
       "    ) " +
       "  )" +
       ") " +
       "AND (:category IS NULL OR p.category = :category) " +
       "AND (:userName IS NULL OR p.owner.username = :userName) " +
       "GROUP BY p " +
       "ORDER BY COUNT(s) DESC")
    Page<BrowseViewPetitonDTO> findAllOrderBySignaturesDescc(Pageable pageable, @Param("title") String title,
            @Param("scope") LocationScope scope,
            @Param("category") Category category,
            @Param("userName") String userName,
            @Param("location") String location);
        
 @Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
       "FROM Petition p " +
       "LEFT JOIN Signee s ON s.petition = p " +
       "WHERE (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
       "AND (" +
       "  :scope IS NULL OR " +
       "  (p.locationScope = :scope AND " +
       "    ( " +
       "      (:scope != 'CITY' OR LOWER(p.locationCity) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
       "      (:scope != 'COUNTRY' OR LOWER(p.locationCountry) LIKE LOWER(CONCAT('%', :location, '%'))) " +
       "    ) " +
       "  )" +
       ") " +
       "AND (:category IS NULL OR p.category = :category) " +
       "AND (:userName IS NULL OR p.owner.username = :userName) " +
       "GROUP BY p " +
       "ORDER BY COUNT(s) ASC")
    Page<BrowseViewPetitonDTO> findAllOrderBySignaturesAscc(Pageable pageable, @Param("title") String title,
            @Param("scope") LocationScope scope,
            @Param("category") Category category,
            @Param("userName") String userName,
            @Param("location") String location);

@Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
       "FROM Petition p " +
       "LEFT JOIN Signee s ON s.petition = p " +
       "WHERE (:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.owner.username) LIKE LOWER(CONCAT('%', :search, '%'))) " +
       "AND (" +
       "  :scope IS NULL OR " +
       "  (p.locationScope = :scope AND " +
       "    ( " +
       "      (:scope != 'CITY' OR LOWER(p.locationCity) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
       "      (:scope != 'COUNTRY' OR LOWER(p.locationCountry) LIKE LOWER(CONCAT('%', :location, '%'))) " +
       "    ) " +
       "  )" +
       ") " +
       "AND (:category IS NULL OR p.category = :category) " +
       "GROUP BY p " +
       "ORDER BY COUNT(s) DESC")
    Page<BrowseViewPetitonDTO> findAllOrderBySignaturesDesccbar(Pageable pageable, @Param("search") String search,
            @Param("scope") LocationScope scope,
            @Param("category") Category category,
            @Param("location") String location);
        
 @Query("SELECT new com.example.speak_up.business_logic.dto.BrowseViewPetitonDTO(p, COUNT(s)) " +
       "FROM Petition p " +
       "LEFT JOIN Signee s ON s.petition = p " +
       "WHERE (:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.owner.username) LIKE LOWER(CONCAT('%', :search, '%'))) " +
       "AND (" +
       "  :scope IS NULL OR " +
       "  (p.locationScope = :scope AND " +
       "    ( " +
       "      (:scope != 'CITY' OR LOWER(p.locationCity) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
       "      (:scope != 'COUNTRY' OR LOWER(p.locationCountry) LIKE LOWER(CONCAT('%', :location, '%'))) " +
       "    ) " +
       "  )" +
       ") " +
       "AND (:category IS NULL OR p.category = :category) " +
       "GROUP BY p " +
       "ORDER BY COUNT(s) ASC")
    Page<BrowseViewPetitonDTO> findAllOrderBySignaturesAsccbar(Pageable pageable, @Param("search") String search,
            @Param("scope") LocationScope scope,
            @Param("category") Category category,
            @Param("location") String location);
}