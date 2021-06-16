package org.angnysa.autodb.metadata.jdbc.relations;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.angnysa.autodb.metadata.api.relations.ManyToOne;
import org.angnysa.autodb.metadata.jdbc.ForeignKey;
import org.angnysa.autodb.metadata.jdbc.JdbcIndex;
import org.angnysa.autodb.metadata.jdbc.JdbcUniqueIndex;

@EqualsAndHashCode(callSuper = true)
@Value
public class JdbcManyToOne extends JdbcRelation implements ManyToOne {
    ForeignKey foreignKey;

    JdbcManyToOne(@NonNull ForeignKey foreignKey) {
        super(foreignKey.getName(), "");
        this.foreignKey = foreignKey;
    }

    @Override
    public boolean isMandatory() {
        return foreignKey.isMandatory();
    }

    @Override
    public JdbcIndex getReferencing() {
        return foreignKey.getReferencing();
    }

    @Override
    public JdbcUniqueIndex getReferenced() {
        return foreignKey.getReferenced();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
