package org.angnysa.autodb.metadata.jdbc;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;
import org.angnysa.autodb.metadata.api.utils.Dumper;

@Value
public class ForeignKey {

    public enum Action {
		NoAction, Restrict, SetDefault, SetNull, Cascade
	}

	public enum ValidationDeferrability {
		NotDeferrable, Deferrable, InitiallyDeferred
	}

	@NonNull String name;
	@NonNull JdbcIndex referencing;
	@NonNull JdbcUniqueIndex referenced;
	@NonNull Action actionOnUpdate;
	@NonNull Action actionOnDelete;
	@NonNull ValidationDeferrability validationDeferrability;

	public ForeignKey(@NonNull String name, @NonNull JdbcIndex referencing, @NonNull JdbcUniqueIndex referenced, @NonNull Action actionOnUpdate, @NonNull Action actionOnDelete, @NonNull ValidationDeferrability validationDeferrability) {

		if (referencing.getColumns().size() != referenced.getColumns().size()) {
			throw new IllegalArgumentException(
					String.format(
							"Cannot create foreign key '%s': Referencing index and referenced indexes must have the same size: %s, %s"
							, name, referencing, referenced));
		}

		this.name = name;
		this.referencing = referencing;
		this.referenced = referenced;
		this.actionOnUpdate = actionOnUpdate;
		this.actionOnDelete = actionOnDelete;
		this.validationDeferrability = validationDeferrability;
	}

	public boolean isMandatory() {
		for (JdbcColumn column : getReferencing().getColumns()) {
			if (column.isNullable()) {
				return false;
			}
		}

		return true;
	}

	@SneakyThrows
	@Override
	public String toString() {
		return String.format("%s '%s' %s %s references %s %s on update %s on delete %s defer %s",
				getClass().getSimpleName(),
				getName(),
				getReferencing().getTable().getFullyQualifiedName(),
				Dumper.columnsName(getReferencing().getColumns()),
				getReferenced().getTable().getFullyQualifiedName(),
				Dumper.columnsName(getReferenced().getColumns()),
				actionOnUpdate,
				actionOnDelete,
				validationDeferrability);
	}
}
