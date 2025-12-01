package com.example.backend.repository.cryteriaAPI;

import com.example.backend.model.entities.Shift;
import com.example.backend.model.request.post.shiftRequests.ShiftSearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class ShiftSearchCriteria implements Specification<Shift> {

    private final ShiftSearchRequest request;

    @Override
    public Predicate toPredicate(
            @NotNull Root<Shift> root,
            @NotNull CriteriaQuery<?> query,
            @NotNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if(request.getShiftDate() != null && !request.getShiftDate().isBlank()){
            predicates.add(criteriaBuilder.equal(
                    root.get(Shift.SHIFT_DATE_FIELD),
                    LocalDate.parse(request.getShiftDate())
            ));
        }
        if (request.getProjectId() != null) {
            predicates.add(criteriaBuilder.equal(
                    root.get("project").get("id"),
                    request.getProjectId()
            ));
        }
        if (request.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(
                    root.get("user").get("id"),
                    request.getUserId()
            ));
        }

        sort(root, criteriaBuilder, query);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void sort(Root<Shift> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query){
        if(Objects.nonNull(request.getShiftSortField())){
            switch(request.getShiftSortField()){
                case SHIFT_DATE -> query.orderBy(criteriaBuilder.desc(root.get(Shift.SHIFT_DATE_FIELD)));
                case USER_ID -> query.orderBy(criteriaBuilder.desc(root.get("user").get("id")));
                default -> query.orderBy(criteriaBuilder.desc(root.get(Shift.ID_SHIFT)));
            }
        }else {
            query.orderBy(criteriaBuilder.desc(root.get(Shift.SHIFT_DATE_FIELD)));
        }
    }
}
