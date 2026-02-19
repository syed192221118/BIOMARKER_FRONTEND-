import logging

logger = logging.getLogger('medical.audit')

def audit_log(user, action, detail):
    """
    Logs medical sensitive actions for audit trail.
    """
    msg = f"User: {user.username} (ID: {user.id}) | Action: {action} | Detail: {detail}"
    logger.info(msg)
