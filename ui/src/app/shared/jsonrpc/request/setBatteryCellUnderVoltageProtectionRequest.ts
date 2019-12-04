import { JsonrpcRequest } from "../base";

/**
 * Sets the write value of a Channel.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": UUID,
 *   "method": "setBatteryCellUnderVoltageProtection",
 *   "params": {
 *     "componentId": string,
 *     "cellUnderVoltageProtection": number,
 *     "cellUnderVoltageProtectionRecover": number
 *   }
 * }
 * </pre>
 */
export class SetBatteryCellUnderVoltageProtectionRequest extends JsonrpcRequest {

    static METHOD: string = "componentJsonApi";

    public constructor(
        public readonly params: {
            cellUnderVoltageProtection: string,
            cellUnderVoltageProtectionRecover: string
        }
    ) {
        super(SetBatteryCellUnderVoltageProtectionRequest.METHOD, params);
    }
}