package org.angnysa.autodb.metadata.jdbc.relations;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.angnysa.autodb.metadata.api.relations.OneToMany;
import org.angnysa.autodb.metadata.jdbc.ForeignKey;
import org.angnysa.autodb.metadata.jdbc.JdbcIndex;
import org.angnysa.autodb.metadata.jdbc.JdbcUniqueIndex;

@EqualsAndHashCode(callSuper = true)
@Value
public class JdbcOneToMany extends JdbcRelation implements OneToMany {
    ForeignKey foreignKey;

    JdbcOneToMany(@NonNull ForeignKey foreignKey) {
        super(foreignKey.getName(), "");
        this.foreignKey = foreignKey;
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public JdbcUniqueIndex getReferencing() {
        return foreignKey.getReferenced();
    }

    @Override
    public JdbcIndex getReferenced() {
        return foreignKey.getReferencing();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
