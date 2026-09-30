alter table members
add constraint uq_members_id_conversation
    unique (id, conversation_id);

alter table messages
add constraint  fk_messages_member_conversation
    foreign key (sender_member_id, conversation_id)
    references members (id, conversation_id);