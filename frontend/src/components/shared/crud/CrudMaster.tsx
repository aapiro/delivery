import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { Box, Button, Heading, HStack, VStack, Checkbox } from "@chakra-ui/react";
import { Plus } from "lucide-react";
import { GenericTable } from "./GenericTable";
import {useNotify} from "../../../hooks/useNotify";

interface CrudMasterProps<T> {
    title: string;
    entityName: string;
    data: T[];
    columns: any[];
    FormComponent: React.ElementType;
    createMutation: any;
    updateMutation: any;
    deleteMutation: any;
    queryKey: any;
}

export function CrudMaster<T extends { id?: any }>({
                                                       title,
                                                       entityName,
                                                       data,
                                                       columns,
                                                       FormComponent,
                                                       createMutation,
                                                       updateMutation,
                                                       deleteMutation,
                                                       queryKey,
                                                   }: CrudMasterProps<T>) {
    const [selectedItem, setSelectedItem] = useState<T | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [createAnother, setCreateAnother] = useState(false);
    const queryClient = useQueryClient();
    const notify = useNotify();

    const refresh = () => queryClient.invalidateQueries({ queryKey });

    const handleCreate = (values: any) => {
        createMutation.mutate({ data: values }, {
            onSuccess: () => {
                notify.success(`${entityName} creado`);
                refresh();
                if (!createAnother) setIsModalOpen(false);
            },
            onError: () => notify.error("Error al crear")
        });
    };

    const handleUpdate = (values: any) => {
        updateMutation.mutate({ id: selectedItem?.id, data: values }, {
            onSuccess: () => {
                notify.success(`${entityName} actualizado`);
                refresh();
                setIsModalOpen(false);
            },
            onError: () => notify.error("Error al actualizar")
        });
    };

    return (
        <VStack align="stretch" gap="6" p="6">
            <HStack justify="space-between">
                <Heading size="lg">{title}</Heading>
                <Button colorPalette="blue" onClick={() => { setSelectedItem(null); setIsModalOpen(true); }}>
                    <Plus size={18} /> Añadir {entityName}
                </Button>
            </HStack>

            <Box bg="white" p="4" rounded="lg" shadow="sm" border="1px solid" borderColor="gray.100">
                <GenericTable
                    data={data}
                    columns={columns}
                    onEdit={(item) => { setSelectedItem(item); setIsModalOpen(true); }}
                    onDelete={(item) => deleteMutation.mutate({ id: item.id }, { onSuccess: refresh })}
                />
            </Box>

            {/* Modal/Dialog de Chakra v3 */}
            {isModalOpen && (
                <Box position="fixed" inset="0" bg="blackAlpha.600" zIndex="1000" display="flex" alignItems="center" justifyContent="center">
                    <Box bg="white" p="8" rounded="xl" shadow="2xl" w="full" maxW="2xl" position="relative">
                        <Heading size="md" mb="6">{selectedItem ? 'Editar' : 'Crear'} {entityName}</Heading>

                        <FormComponent
                            initialData={selectedItem}
                            onSubmit={selectedItem ? handleUpdate : handleCreate}
                            onCancel={() => setIsModalOpen(false)}
                            isLoading={createMutation.isPending || updateMutation.isPending}
                        />

                        {!selectedItem && (
                            <HStack mt="4">
                                <Checkbox.Root checked={createAnother} onCheckedChange={(e: { checked: any; }) => setCreateAnother(!!e.checked)}>
                                    <Checkbox.HiddenInput />
                                    <Checkbox.Control />
                                    <Checkbox.Label textStyle="sm">Crear otro tras guardar</Checkbox.Label>
                                </Checkbox.Root>
                            </HStack>
                        )}
                    </Box>
                </Box>
            )}
        </VStack>
    );
}