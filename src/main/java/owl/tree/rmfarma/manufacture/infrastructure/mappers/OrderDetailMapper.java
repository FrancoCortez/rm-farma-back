package owl.tree.rmfarma.manufacture.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;
import owl.tree.rmfarma.domain.infrastructure.entities.Via;
import owl.tree.rmfarma.domain.infrastructure.mappers.ViaMapper;
import owl.tree.rmfarma.domain.infrastructure.mappers.ViaMapperImpl;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.infrastructure.entities.MasterOrder;
import owl.tree.rmfarma.manufacture.infrastructure.entities.OrderDetail;
import owl.tree.rmfarma.product.infrastructure.entities.Complement;
import owl.tree.rmfarma.product.infrastructure.entities.Product;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    ViaMapper viaMapper = new ViaMapperImpl();
    CommercialOrderDetailMapper commercialOrderDetailMapper = new CommercialOrderDetailMapperImpl();

    default OrderDetailResourceDto toOrderDetailResourceDto(OrderDetail entity) {
        if (entity == null) return null;
        OrderDetailResourceDto resource = new OrderDetailResourceDto();
        BeanUtils.copyProperties(entity, resource);
        if(entity.getVia() != null) {
            resource.setVia(viaMapper.toViaResourceDto(entity.getVia()));
        }
        if(entity.getCommercialOrderDetails() != null) {
            resource.setCommercialOrderDetails(entity.getCommercialOrderDetails().stream().map(commercialOrderDetailMapper::toResourceDto).collect(Collectors.toSet()));
        }
        return resource;
    }

    default OrderDetail toOrderDetailEntity(OrderDetailCreateResourceDto resource) {
        if (resource == null) return null;
        OrderDetail entity = new OrderDetail();
        BeanUtils.copyProperties(resource, entity);
        entity.setQuantity(resource.getQuantity());
        if (resource.getProduct() != null && !resource.getProduct().isEmpty()) {
            entity.setProduct(Product.builder().id(resource.getProduct()).build());
        }
        if (resource.getVia() != null && !resource.getVia().isEmpty()) {
            entity.setVia(Via.builder().id(resource.getVia()).build());
        }
        if (resource.getMasterOrder() != null && !resource.getMasterOrder().isEmpty()) {
            entity.setMasterOrder(MasterOrder.builder().id(resource.getMasterOrder()).build());
        }
        if (resource.getComplement() != null && !resource.getComplement().isEmpty()) {
            entity.setComplement(Complement.builder().id(resource.getComplement()).build());
        }
        return entity;
    }


}
