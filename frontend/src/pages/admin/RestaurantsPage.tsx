import {
    useListRestaurants,
    useCreateRestaurant,
    useUpdateRestaurant,
    //useDeleteRestaurant,
    getListRestaurantsQueryKey
} from "../../api/generated/administrative-api/administrative-api";
import { CrudMaster } from "../../components/shared/crud/CrudMaster";
import RestaurantForm from "./RestaurantForm"; // Tu form existente

export default function RestaurantsPage() {
    const { data } = useListRestaurants();
    const createMutation = useCreateRestaurant();
    const updateMutation = useUpdateRestaurant();

    function useDeleteRestaurant() {
        // get this from administrative-api
    }

    const deleteMutation = useDeleteRestaurant();

    const columns = [
        { header: "Nombre", accessor: "name" },
        { header: "Cocina", accessor: "cuisine" },
        { header: "Estado", accessor: (item: any) => item.isOpen ? "Abierto" : "Cerrado" },
    ];

    return (
        <CrudMaster
            title="Gestión de Restaurantes"
            entityName="Restaurante"
            data={data?.content || []} // Si usas paginación
            columns={columns}
            FormComponent={RestaurantForm}
            createMutation={createMutation}
            updateMutation={updateMutation}
            deleteMutation={deleteMutation}
            queryKey={getListRestaurantsQueryKey()} // Clave de Orval para refrescar
        />
    );
}