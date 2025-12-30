import { Table, IconButton, HStack, Badge } from "@chakra-ui/react";
import { Edit, Trash2, Eye } from "lucide-react";

interface Column<T> {
    header: string;
    accessor: keyof T | ((item: T) => React.ReactNode);
}

interface GenericTableProps<T> {
    data: T[];
    columns: Column<T>[];
    onEdit: (item: T) => void;
    onDelete: (item: T) => void;
    onView?: (item: T) => void;
    isLoading?: boolean;
}

export function GenericTable<T extends { id?: string | number }>({
                                                                     data,
                                                                     columns,
                                                                     onEdit,
                                                                     onDelete,
                                                                     onView,
                                                                     isLoading,
                                                                 }: GenericTableProps<T>) {
    return (
        <Table.Root size="sm" variant="line" stickyHeader>
            <Table.Header>
                <Table.Row bg="gray.50">
                    {columns.map((col, i) => (
                        <Table.ColumnHeader key={i}>{col.header}</Table.ColumnHeader>
                    ))}
                    <Table.ColumnHeader textAlign="end">Acciones</Table.ColumnHeader>
                </Table.Row>
            </Table.Header>
            <Table.Body>
                {data.map((item, rowIndex) => (
                    <Table.Row key={item.id || rowIndex} _hover={{ bg: "gray.50" }}>
                        {columns.map((col, i) => (
                            <Table.Cell key={i}>
                                {typeof col.accessor === "function"
                                    ? col.accessor(item)
                                    : (item[col.accessor] as React.ReactNode)}
                            </Table.Cell>
                        ))}
                        <Table.Cell textAlign="end">
                            <HStack justify="end" gap="2">
                                {onView && (
                                    <IconButton variant="ghost" size="sm" onClick={() => onView(item)}>
                                        <Eye size={16} />
                                    </IconButton>
                                )}
                                <IconButton variant="ghost" size="sm" colorPalette="blue" onClick={() => onEdit(item)}>
                                    <Edit size={16} />
                                </IconButton>
                                {/*<IconButton variant="ghost" size="sm" colorPalette="red" onClick={() => onDelete(item)}>
                                    <Trash2 size={16} />
                                </IconButton>*/}
                            </HStack>
                        </Table.Cell>
                    </Table.Row>
                ))}
            </Table.Body>
        </Table.Root>
    );
}