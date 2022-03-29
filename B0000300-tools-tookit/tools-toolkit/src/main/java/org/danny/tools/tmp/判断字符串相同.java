/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author wusui
 * @version $Id: Test.java, 2020-12-03 2:02 PM wusui Exp $
 */
public class 判断字符串相同 {

    static int cnt = 0;

    /**
     * LOGGER
     */
    public static void main(String[] args) {
        String str1 = "ID: 4ae3d21c032bce09b153358d6f5c566b    Result properties: [(string-TenantOwner:Bell IT), (string_list-TenantsUses:[Bell IT]), (string-bios_asset_tag:[Empty CMDB property value]), (string-bios_uuid:39373638-3935-584D-5139-343830315848), (string-cloud_instance_id:[Empty CMDB property value]), (string-global_id:a042e532bcbbed4476ac750054c0dabb), (string-host_nnm_uid:[Empty CMDB property value]), (string-name:dc5cn2), (string-net_bios_name:[Empty CMDB property value]), (string-os_family:unix), (string-primary_dns_name:dc5cn2.bellca.int.bell.ca), (string-snmp_sys_name:[Empty CMDB property value])]";
        String str2 = "ID: 4ae3d21c032bce09b153358d6f5c566b    Result properties: [(string-TenantOwner:Bell IT), (string_list-TenantsUses:[Bell IT]), (string-bios_asset_tag:[Empty CMDB property value]), (string-bios_uuid:39373638-3935-584D-5139-343830315848), (string-cloud_instance_id:[Empty CMDB property value]), (string-global_id:a042e532bcbbed4476ac750054c0dabb), (string-host_nnm_uid:[Empty CMDB property value]), (string-name:dc5cn2), (string-net_bios_name:[Empty CMDB property value]), (string-os_family:unix), (string-primary_dns_name:dc5cn2.bellca.int.bell.ca), (string-serial_number:MXQ94801XH), (string-snmp_sys_name:[Empty CMDB property value])]";
        System.out.println(str1.equals(str2));
    }

}
