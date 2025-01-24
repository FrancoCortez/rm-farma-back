package owl.tree.rmfarma.manufacture.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.commercialorderdetail.CommercialOrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.CommercialOrderDetail;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;
import owl.tree.rmfarma.product.infrastructure.entities.CommercialProduct;
import owl.tree.rmfarma.product.infrastructure.mappers.CommercialProductMapper;
import owl.tree.rmfarma.product.infrastructure.mappers.CommercialProductMapperImpl;

@Mapper(componentModel = "spring")
public interface CommercialOrderDetailMapper {
    CommercialProductMapper commercialProductMapper = new CommercialProductMapperImpl();

    default CommercialOrderDetailResourceDto toResourceDto(CommercialOrderDetail entity) {
        CommercialOrderDetailResourceDto dto = new CommercialOrderDetailResourceDto();
        BeanUtils.copyProperties(entity, dto);
        if(entity.getCommercialProduct() != null) {
            dto.setCommercialProduct(commercialProductMapper.toCommercialProductResourceDto(entity.getCommercialProduct()));
        }
        return dto;
    }

    default CommercialOrderDetail toCommercialOrderDetailEntity(CommercialOrderDetailCreateResourceDto dto) {
        CommercialOrderDetail entity = new CommercialOrderDetail();
        BeanUtils.copyProperties(dto, entity);
        if(dto.getCommercialProduct() != null) {
            entity.setCommercialProduct(CommercialProduct.builder().id(dto.getCommercialProduct()).build());
        }
        if(dto.getOrderDetail() != null) {
            entity.setOrderDetail(OrderDetail.builder().id(dto.getOrderDetail()).build());
        }
        return entity;
    }
}
