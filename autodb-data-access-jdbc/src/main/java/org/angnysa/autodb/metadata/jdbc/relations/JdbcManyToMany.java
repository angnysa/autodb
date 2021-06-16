package org.angnysa.autodb.metadata.jdbc.relations;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.angnysa.autodb.metadata.api.relations.ManyToMany;
import org.angnysa.autodb.metadata.jdbc.ForeignKey;
import org.angnysa.autodb.metadata.jdbc.JdbcUniqueIndex;

@EqualsAndHashCode(callSuper = true)
@Value
public class JdbcManyToMany extends JdbcRelation implements ManyToMany {
    ForeignKey foreignKey1;
    ForeignKey foreignKey2;

    public JdbcManyToMany(@NonNull ForeignKey foreignKey1, @NonNull ForeignKey foreignKey2) {
        super(foreignKey1.getName(), "");
        this.foreignKey1 = foreignKey1;
        this.foreignKey2 = foreignKey2;
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public JdbcUniqueIndex getReferencing() {
        return foreignKey1.getReferenced();
    }

    @Override
    public JdbcUniqueIndex getReferenced() {
        return foreignKey2.getReferenced();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
