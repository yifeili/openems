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
 *     "bmsId": string,
 *     "cellUnderVoltageProtection": number,
 *     "cellUnderVoltageProtectionRecover": number
 *   }
 * }
 * </pre>
 */
export class SetBatteryCellUnderVoltageProtectionRequest extends JsonrpcRequest {

    static METHOD: string = "setBatteryCellUnderVoltageProtection";

    public constructor(
        public readonly params: {
            bmsId: string,
            cellUnderVoltageProtection: number,
            cellUnderVoltageProtectionRecover: number
        }
    ) {
        super(SetBatteryCellUnderVoltageProtectionRequest.METHOD, params);
    }
}