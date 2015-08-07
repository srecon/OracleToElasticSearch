package com.blu.es.mapper;


import com.blu.es.dto.TableObjectsDTO;
import com.blu.es.dto.UserObjectDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shamim on 07/08/15.
 */
public class TableRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
/*        UserObjectDTO userObjectDTO = new UserObjectDTO();
        userObjectDTO.setObjectId(resultSet.getString("OBJECT_ID"));
        userObjectDTO.setObjectName(resultSet.getString("OBJECT_NAME"));
        return userObjectDTO;*/
        TableObjectsDTO tableObjectsDTO = new TableObjectsDTO();
        tableObjectsDTO.setTableName(resultSet.getString("object_name"));
        tableObjectsDTO.setColumnName(resultSet.getString("column_name"));

        return tableObjectsDTO;
    }
}
