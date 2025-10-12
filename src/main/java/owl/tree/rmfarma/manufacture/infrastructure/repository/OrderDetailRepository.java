package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.ConcentrationReportDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.ResumeReportDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    @Query(value = """
    SELECT od.master_record AS masterRecord,
           od.production_date AS productionDate,
           CONCAT(mo.patient_name, ' ', mo.patient_last_name) AS fullPatientName,
           mo.patient_rut AS patientRut,
           mo.doctor_name AS doctorName,
           mo.doctor_rut AS doctorRut,
           mo.unit_name_name AS unitName,
           cod.code AS code,
           cod.commercial AS description,
           cod.quantity AS quantity,
           CONCAT(od.quantity, ' ', od.unit_metric) AS quantityReal,
           cod.laboratory AS laboratory,
           cod.batch AS batch,
           mo.isapre_code AS isapreCode,
           mo.isapre_name AS isapreName,
           mo.diagnosis_code AS diagnosisCode,
           mo.diagnosis_name AS diagnosisName,
           mo.cycle_day AS cycleDay,
           mo.cycle_number AS cycleNumber,
           mo.schema_code AS schemaCode,
           mo.schema_name AS schemaName,
           0 AS volumeTotal,
           od.via_code AS viaCode,
           od.via_description AS viaDescription,
           od.pharmaceutical_chemist AS qf,
           0 AS guia
    FROM order_detail od
    INNER JOIN master_order mo ON od.master_order_id = mo.id
    INNER JOIN commercial_order_detail cod ON od.id = cod.order_detail_id
    WHERE od.production_date BETWEEN :startDate AND :endDate
    UNION ALL
    SELECT od.master_record AS masterRecord,
           od.production_date AS productionDate,
           CONCAT(mo.patient_name, ' ', mo.patient_last_name) AS fullPatientName,
           mo.patient_rut AS patientRut,
           mo.doctor_name AS doctorName,
           mo.doctor_rut AS doctorRut,
           mo.unit_name_name AS unitName,
           c.code AS code,
           c.description AS description,
           1 AS quantity,
           '' AS quantityReal,
           '' AS laboratory,
           '' AS batch,
           mo.isapre_code AS isapreCode,
           mo.isapre_name AS isapreName,
           mo.diagnosis_code AS diagnosisCode,
           mo.diagnosis_name AS diagnosisName,
           mo.cycle_day AS cycleDay,
           mo.cycle_number AS cycleNumber,
           mo.schema_code AS schemaCode,
           mo.schema_name AS schemaName,
           od.volume_total AS volumeTotal,
           od.via_code AS viaCode,
           od.via_description AS viaDescription,
           od.pharmaceutical_chemist AS qf,
           0 AS guia
    FROM order_detail od
    INNER JOIN master_order mo ON od.master_order_id = mo.id
    INNER JOIN complement c ON od.complement_id = c.id
    WHERE od.production_date BETWEEN :startDate AND :endDate
    ORDER BY masterRecord DESC
    """, nativeQuery = true)
    List<CustomReportDTO> getCustomReport(OffsetDateTime startDate, OffsetDateTime endDate);

    Optional<OrderDetail> findByMasterRecord(String masterRecord);

    @Query(value = """
                    SELECT mo.patient_rut       AS rut,
                           mo.schema_name       AS schemaName,
                           mo.unit_name_name    AS unitHospitalName,
                           mo.doctor_name       AS doctorName,
                           od.master_record     AS masterRecord,
                           mo.patient_last_name AS lastName,
                           mo.patient_name      AS name,
                           od.production_date   AS productionDate,
                           od.observation       AS observation,
                           CONCAT(od.product_name, ' ', od.quantity, ' ', unit_metric, ' + ', od.complement_name, ' ', od.volume_total , ' ml') AS productName,
                           mo.cycle_number      AS cycleNumber,
                           mo.cycle_day         AS cycleDay,
                           0 AS ov,
                           'N/A' AS trainer
                    FROM master_order mo
                    INNER JOIN order_detail od ON mo.id = od.master_order_id
                    WHERE od.production_date BETWEEN :startDate AND :endDate
                    ORDER BY masterRecord DESC
            """, nativeQuery = true)
    List<ResumeReportDto> getResumeReport(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query(value = """
                    select CONCAT(mo.patient_name, ' ', mo.patient_last_name)                                              as name,
                        mo.unit_name_name                                                                                  as hospitalName,
                        od.product_name                                                                                    as productName,
                        od.quantity                                                                                        as dose,
                        od.unit_metric                                                                                     as unitMetric,
                        od.concentration                                                                                   as volumeTotalProduct,
                        (select cod.laboratory from commercial_order_detail cod where cod.order_detail_id = od.id limit 1) as laboratory,
                        od.volume_total                                                                                    as volumeTotal,
                        od.complement_name                                                                                 as vehicle,
                        od.via_description                                                                                 as viaName,
                        ''                                                                                                 as down
                    from master_order mo
                    inner join order_detail od on od.master_order_id = mo.id
                    WHERE od.production_date BETWEEN :startDate AND :endDate
            """, nativeQuery = true)
    List<ConcentrationReportDto> getConcentrationReport(OffsetDateTime startDate, OffsetDateTime endDate);

}
