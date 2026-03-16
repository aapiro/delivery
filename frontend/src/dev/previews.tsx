import {ComponentPreview, Previews} from "@react-buddy/ide-toolbox";
import {PaletteTree} from "./palette";
import {Toaster} from "../components/ui/toaster";
import {Input} from "../components/ui";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/Toaster">
                <Toaster/>
            </ComponentPreview>
            <ComponentPreview path="/Input">
                <Input/>
            </ComponentPreview>
        </Previews>
    );
};

export default ComponentPreviews;