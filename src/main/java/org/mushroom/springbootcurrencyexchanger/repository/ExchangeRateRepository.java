package org.mushroom.springbootcurrencyexchanger.repository;

import org.mushroom.springbootcurrencyexchanger.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT er FROM ExchangeRate er " +
           "JOIN FETCH er.baseCurrency " +
           "JOIN FETCH er.targetCurrency")
    List<ExchangeRate> findAllFetchFields();

    @Query("SELECT er FROM ExchangeRate er " +
           "JOIN FETCH er.baseCurrency bc " +
           "JOIN FETCH er.targetCurrency tc " +
           "WHERE bc.code = :baseCode " +
           "AND tc.code = :targetCode")
    Optional<ExchangeRate> findByCurrencyPair(@Param("baseCode") String baseCode, @Param("targetCode") String targetCode);

    @Query("SELECT er FROM ExchangeRate er " +
           "JOIN FETCH er.baseCurrency " +
           "JOIN FETCH er.targetCurrency " +
           "WHERE er.id = :id")
    Optional<ExchangeRate> findByIdWithCurrencies(@Param("id") Long id);
}
