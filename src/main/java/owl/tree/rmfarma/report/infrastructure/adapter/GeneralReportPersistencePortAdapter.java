package owl.tree.rmfarma.report.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import owl.tree.rmfarma.report.domain.data.ChemotherapyPreparationFormDto;
import owl.tree.rmfarma.report.domain.data.RecipeBookDto;
import owl.tree.rmfarma.report.domain.ports.spi.GeneralReportPersistencePort;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GeneralReportPersistencePortAdapter implements GeneralReportPersistencePort {
    private final JdbcTemplate jdbcTemplate;


    public List<RecipeBookDto> recipeBook (OffsetDateTime startDate, OffsetDateTime endDate) {
        String query = """
                select od.master_record                                   as masterRecord,
                       od.production_date                                 as productionDate,
                       concat(mo.patient_name, ' ', mo.patient_last_name) as patientName,
                       mo.patient_rut                                     as patientRut,
                       mo.doctor_name                                     as doctorName,
                       mo.doctor_rut                                      as doctorRut,
                       od.product_name                                    as productName,
                       concat(od.quantity, ' ', od.unit_metric)           as dose,
                       cod.laboratory                                     as laboratory,
                       cod.batch                                          as lote,
                       od.expiration_date_date                            as expirationDate,
                       od.complement_name                                 as complementName,
                       concat(od.volume_total, ' ml')                     as volumeTotal
                from master_order mo
                         inner join order_detail od on mo.id = od.master_order_id
                         inner join commercial_order_detail cod on od.id = cod.order_detail_id
                where od.production_date BETWEEN ? AND ?
                
                """;
        return this.jdbcTemplate.query(
                query,
                new BeanPropertyRowMapper<>(RecipeBookDto.class),
                Timestamp.from(startDate.toInstant()),
                Timestamp.from(endDate.toInstant())
        );
    }

    public List<ChemotherapyPreparationFormDto> chemotherapyPreparationForm(OffsetDateTime startDate, OffsetDateTime endDate) {
        String query = """
                SELECT
                od.production_date AS productionDate,
                od.master_record AS masterRecord,
                CONCAT(mo.patient_name, ' ', mo.patient_last_name) AS fullPatientName,
                mo.patient_rut AS patientRut,
                (select s.description from services s where s.id = dp.services_id) AS service,
                od.bed_day AS bed,
                mo.patient_identification as patientIdentification,
                mo.diagnosis_name AS diagnosis,
                mo.schema_name AS schema_de,
                mo.cycle_number AS cycleNumber,
                mo.cycle_day AS cycleDay,
                mo.doctor_name AS doctorName,
                mo.doctor_rut AS doctorRut,
                od.product_name AS activeIngredient,
                CONCAT(od.quantity, ' ', od.unit_metric) AS dose,
                cod.batch AS batch,
                cod.laboratory AS laboratory,
                now() AS expirationDate,
                CONCAT(od.volume_total, ' ML') AS volumeTotal,
                od.complement_name AS complement,
                od.via_description AS via,
                od.administration_time AS administrationTime,
                od.administration_date AS administrationDate,
                od.expiration_date_date AS expirationAdministrationDate,
                od.conditional AS conditionValue,
                od.observation AS observation,
                '' AS qfValidate,
                '' AS qfPrepare,
                '' AS technicalPrepare,
                '' AS qfConditioningTechnician
            FROM order_detail od
                INNER JOIN master_order mo ON od.master_order_id = mo.id
            INNER JOIN diagnosis_order_stage dos ON dos.id = mo.diagnosis_order_stage_id
                    INNER JOIN commercial_order_detail cod ON od.id = cod.order_detail_id
            INNER JOIN diagnosis_patient dp on dp.id = dos.diagnosis_patient_id
            where od.production_date BETWEEN ? AND ?
            order by od.production_date desc
            """;
        return this.jdbcTemplate.query(
                query,
                new BeanPropertyRowMapper<>(ChemotherapyPreparationFormDto.class),
                Timestamp.from(startDate.toInstant()),
                Timestamp.from(endDate.toInstant())
        );
    }
}
