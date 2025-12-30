import {toaster} from "../components/ui/toaster";

export const useNotify = () => {
    const success = (title: string, description?: string) =>
        toaster.create({ title, description, type: "success" });

    const error = (title: string, description?: string) =>
        toaster.create({ title, description, type: "error" });

    return { success, error };
};