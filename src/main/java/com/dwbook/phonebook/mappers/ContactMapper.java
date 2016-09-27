package com.dwbook.phonebook.mappers;

import com.dwbook.phonebook.representations.Contact;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by objectedge on 9/27/16.
 */
public class ContactMapper implements ResultSetMapper<Contact>{
    @Override
    public Contact map(int index, ResultSet resultSet,
                       StatementContext context) throws SQLException {
        return new Contact(resultSet.getInt("id"), resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("phone"));
    }
}
