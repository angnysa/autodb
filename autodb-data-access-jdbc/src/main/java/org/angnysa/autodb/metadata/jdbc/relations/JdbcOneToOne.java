package org.angnysa.autodb.metadata.jdbc.relations;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.angnysa.autodb.metadata.api.relations.OneToOne;
import org.angnysa.autodb.metadata.jdbc.ForeignKey;
import org.angnysa.autodb.metadata.jdbc.JdbcUniqueIndex;

@EqualsAndHashCode(callSuper = true)
@Value
public class JdbcOneToOne extends JdbcRelation implements OneToOne {
    ForeignKey foreignKey;
    boolean inverted;

    JdbcOneToOne(@NonNull ForeignKey foreignKey, boolean inverted) {
        super(foreignKey.getName(), "");
        this.foreignKey = foreignKey;
        this.inverted = inverted;
    }

    @Override
    public boolean isMandatory() {
        if (inverted) {
            return false;
        } else {
            return foreignKey.isMandatory();
        }
    }

    @Override
    public JdbcUniqueIndex getReferencing() {
        if (inverted) {
            return foreignKey.getReferenced();
        } else {
            return (JdbcUniqueIndex) foreignKey.getReferencing();
        }
    }

    @Override
    public JdbcUniqueIndex getReferenced() {
        if (inverted) {
            return (JdbcUniqueIndex) foreignKey.getReferencing();
        } else {
            return foreignKey.getReferenced();
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
