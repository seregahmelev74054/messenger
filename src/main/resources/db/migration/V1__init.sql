--
-- PostgreSQL database dump
--



-- Dumped from database version 17.10
-- Dumped by pg_dump version 17.10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: conversations; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conversations (
    id bigint NOT NULL,
    type character varying(255),
    visibility character varying(255),
    created_at timestamp(6) without time zone,
    title character varying(255),
    updated_at timestamp(6) without time zone,
    deleted_at timestamp(6) without time zone,
    direct_value character varying(255),
    CONSTRAINT conversations_type_check CHECK (((type)::text = ANY ((ARRAY['DIRECT'::character varying, 'GROUP'::character varying])::text[]))),
    CONSTRAINT conversations_visibility_check CHECK (((visibility)::text = ANY ((ARRAY['PRIVATE'::character varying, 'OPEN'::character varying])::text[])))
);


--
-- Name: conversations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.conversations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: conversations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.conversations_id_seq OWNED BY public.conversations.id;


--
-- Name: members; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.members (
    id bigint NOT NULL,
    status character varying(255),
    conversation_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT members_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'LEFT'::character varying, 'BANNED'::character varying])::text[])))
);


--
-- Name: members_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.members_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: members_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.members_id_seq OWNED BY public.members.id;


--
-- Name: message_deletions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.message_deletions (
    id bigint NOT NULL,
    deleted_at timestamp(6) without time zone,
    member_id bigint NOT NULL,
    message_id bigint NOT NULL
);


--
-- Name: message_deletions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.message_deletions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: message_deletions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.message_deletions_id_seq OWNED BY public.message_deletions.id;


--
-- Name: messages; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.messages (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    deleted_at timestamp(6) without time zone,
    text character varying(255),
    conversation_id bigint NOT NULL,
    sender_member_id bigint NOT NULL
);


--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.messages_id_seq OWNED BY public.messages.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    display_name character varying(255),
    email character varying(255),
    login character varying(255),
    password_hash character varying(255)
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: conversations id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conversations ALTER COLUMN id SET DEFAULT nextval('public.conversations_id_seq'::regclass);


--
-- Name: members id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.members ALTER COLUMN id SET DEFAULT nextval('public.members_id_seq'::regclass);


--
-- Name: message_deletions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_deletions ALTER COLUMN id SET DEFAULT nextval('public.message_deletions_id_seq'::regclass);


--
-- Name: messages id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages ALTER COLUMN id SET DEFAULT nextval('public.messages_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: conversations conversations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT conversations_pkey PRIMARY KEY (id);


--
-- Name: members members_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT members_pkey PRIMARY KEY (id);


--
-- Name: message_deletions message_deletions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_deletions
    ADD CONSTRAINT message_deletions_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: members ukak5rybfcxjirxsdtcjgyswq3l; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT ukak5rybfcxjirxsdtcjgyswq3l UNIQUE (user_id, conversation_id);


--
-- Name: conversations ukhk3n9g3i3l90a7re2fi3d0c7h; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT ukhk3n9g3i3l90a7re2fi3d0c7h UNIQUE (direct_value);


--
-- Name: message_deletions ukl0ycbpimakaxaey1b0qr48i0v; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_deletions
    ADD CONSTRAINT ukl0ycbpimakaxaey1b0qr48i0v UNIQUE (message_id, member_id);


--
-- Name: users ukow0gan20590jrb00upg3va2fn; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukow0gan20590jrb00upg3va2fn UNIQUE (login);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: idx_message_conversationid_createdat_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_message_conversationid_createdat_id ON public.messages USING btree (conversation_id, created_at, id);


--
-- Name: message_deletions fk6gp0lghg7vk0h5cpsvxuxn8yp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_deletions
    ADD CONSTRAINT fk6gp0lghg7vk0h5cpsvxuxn8yp FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- Name: message_deletions fkb20eaax7w9werkoesafggp1a6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_deletions
    ADD CONSTRAINT fkb20eaax7w9werkoesafggp1a6 FOREIGN KEY (member_id) REFERENCES public.members(id);


--
-- Name: messages fkj5w6m5ggn0ueo7np6fylokgpv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkj5w6m5ggn0ueo7np6fylokgpv FOREIGN KEY (sender_member_id) REFERENCES public.members(id);


--
-- Name: members fkpj3n6wh5muoeakc485whgs3x5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT fkpj3n6wh5muoeakc485whgs3x5 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: messages fkt492th6wsovh1nush5yl5jj8e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkt492th6wsovh1nush5yl5jj8e FOREIGN KEY (conversation_id) REFERENCES public.conversations(id);


--
-- Name: members fkt5trkf7csuvhinh3hvobt87aq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT fkt5trkf7csuvhinh3hvobt87aq FOREIGN KEY (conversation_id) REFERENCES public.conversations(id);


--
-- PostgreSQL database dump complete
--



