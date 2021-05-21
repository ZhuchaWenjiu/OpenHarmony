#include <stdio.h>
#include <unistd.h>
#include "ohos_init.h"
#include "cmsis_os2.h"
#include <at.h>
#include <hi_at.h>
hi_u32 at_exe_myproject_cmd(void)
{

    hi_at_printf("at_exe_myproject_cmd\r\n");
    return HI_ERR_SUCCESS;
}

const at_cmd_func g_at_test_func_tbl[] = {
    {"+MYPROJECT", 10, HI_NULL, HI_NULL, HI_NULL, (at_call_back_func)at_exe_myproject_cmd},
};

void AtExampleEntry(void)
{
    hi_at_register_cmd(g_at_test_func_tbl, sizeof(g_at_test_func_tbl) / sizeof(g_at_test_func_tbl[0]));
}

SYS_RUN(AtExampleEntry);
