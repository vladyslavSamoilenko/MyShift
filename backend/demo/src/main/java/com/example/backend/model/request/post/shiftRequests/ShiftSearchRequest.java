package com.example.backend.model.request.post.shiftRequests;

import com.example.backend.model.enums.ShiftSortField;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShiftSearchRequest implements Serializable {
    private String shiftDate;
    private Integer projectId;
    private Integer userId;

    private ShiftSortField shiftSortField;
}
