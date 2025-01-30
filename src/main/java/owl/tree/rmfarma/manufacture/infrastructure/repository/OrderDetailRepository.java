package owl.tree.rmfarma.manufacture.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    @Query(value = "SELECT od.master_record AS masterRecord, " +
            "od.production_date AS productionDate, " +
            "CONCAT(mo.patient_name, ' ', mo.patient_last_name) AS fullPatientName, " +
            "mo.patient_rut AS patientRut, " +
            "mo.doctor_name AS doctorName, " +
            "mo.doctor_rut AS doctorRut, " +
            "mo.unit_name_name AS unitName, " +
            "cod.code AS code, " +
            "cod.commercial AS description, " +
            "cod.quantity AS quantity, " +
            "CONCAT(od.quantity, ' ', od.unit_metric) AS quantityReal, " +
            "cod.laboratory AS laboratory, " +
            "cod.batch AS batch, " +
            "mo.isapre_code AS isapreCode, " +
            "mo.isapre_name AS isapreName, " +
            "mo.diagnosis_code AS diagnosisCode, " +
            "mo.diagnosis_name AS diagnosisName, " +
            "mo.cycle_day AS cycleDay, " +
            "mo.cycle_number AS cycleNumber, " +
            "mo.schema_code AS schemaCode, " +
            "mo.schema_name AS schemaName, " +
            "0 AS volumeTotal, " +
            "od.via_code AS viaCode, " +
            "od.via_description AS viaDescription, " +
            "'QF' AS qf, " +
            "0 AS guia " +
            "FROM order_detail od " +
            "INNER JOIN master_order mo ON od.master_order_id = mo.id " +
            "INNER JOIN commercial_order_detail cod ON od.id = cod.order_detail_id " +
            "UNION ALL " +
            "SELECT od.master_record AS masterRecord, " +
            "od.production_date AS productionDate, " +
            "CONCAT(mo.patient_name, ' ', mo.patient_last_name) AS fullPatientName, " +
            "mo.patient_rut AS patientRut, " +
            "mo.doctor_name AS doctorName, " +
            "mo.doctor_rut AS doctorRut, " +
            "mo.unit_name_name AS unitName, " +
            "c.code AS code, " +
            "c.description AS description, " +
            "1 AS quantity, " +
            "CONCAT(od.quantity, ' ', od.unit_metric) AS quantityReal, " +
            "'' AS laboratory, " +
            "'' AS batch, " +
            "mo.isapre_code AS isapreCode, " +
            "mo.isapre_name AS isapreName, " +
            "mo.diagnosis_code AS diagnosisCode, " +
            "mo.diagnosis_name AS diagnosisName, " +
            "mo.cycle_day AS cycleDay, " +
            "mo.cycle_number AS cycleNumber, " +
            "mo.schema_code AS schemaCode, " +
            "mo.schema_name AS schemaName, " +
            "od.volume_total AS volumeTotal, " +
            "od.via_code AS viaCode, " +
            "od.via_description AS viaDescription, " +
            "'QF' AS qf, " +
            "0 AS guia " +
            "FROM order_detail od " +
            "INNER JOIN master_order mo ON od.master_order_id = mo.id " +
            "INNER JOIN complement c ON od.complement_id = c.id " +
            "ORDER BY masterRecord DESC", nativeQuery = true)
    List<CustomReportDTO> getCustomReport();
}
